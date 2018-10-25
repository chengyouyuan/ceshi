package com.winhxd.b2c.message.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.message.condition.MessageNeteaseCondition;
import com.winhxd.b2c.common.domain.message.condition.NeteaseAccountCondition;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgBoxCondition;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgReadStatusCondition;
import com.winhxd.b2c.common.domain.message.model.MessageNeteaseAccount;
import com.winhxd.b2c.common.domain.message.vo.NeteaseAccountVO;
import com.winhxd.b2c.common.domain.message.vo.NeteaseMsgVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.GeneratePwd;
import com.winhxd.b2c.message.dao.MessageNeteaseAccountMapper;
import com.winhxd.b2c.message.dao.MessageNeteaseHistoryMapper;
import com.winhxd.b2c.message.service.NeteaseService;
import com.winhxd.b2c.message.support.annotation.MessageEnumConvertAnnotation;
import com.winhxd.b2c.message.utils.NeteaseUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jujinbiao
 * @className NeteaseServiceImpl
 * @description
 */
@Service
public class NeteaseServiceImpl implements NeteaseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NeteaseServiceImpl.class);

	private static final String SUCCESS_CODE = "200";
	private static final String ERROR_CODE = "414";
	private static final String ERROR_MSG = "not register";
	private static final String PARAM_CODE = "code";
	private static final String PARAM_DESC = "desc";
	private static final Short TIME_TYPE_TODAY = 0;

	@Value("${netease.accidSuffix}")
	private String accidSuffix;

	@Autowired
	MessageNeteaseAccountMapper neteaseAccountMapper;
	@Autowired
    MessageNeteaseHistoryMapper neteaseHistoryMapper;
	@Autowired
	NeteaseUtils neteaseUtils;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public NeteaseAccountVO getNeteaseAccountInfo(NeteaseAccountCondition neteaseAccountCondition) {
		NeteaseAccountVO result = new NeteaseAccountVO();
		Long customerId = neteaseAccountCondition.getCustomerId();
		if (customerId == null) {
			return null;
		}
		//查询云信账户信息
		MessageNeteaseAccount neteaseAccount = neteaseAccountMapper.getNeteaseAccountByCustomerId(customerId);
		if (neteaseAccount != null) {
			result.setAccid(neteaseAccount.getAccid());
			result.setToken(neteaseAccount.getToken());
			return result;
		}
		//从云信获取该用户的信息，判断该用户是否在云信已经存在
		String accidUinfo = "[\"" + customerId + accidSuffix + "\"]";
		Map<String, Object> userInfo = neteaseUtils.getUserInfo(accidUinfo);
		String codeMes = String.valueOf(userInfo.get(PARAM_CODE));
		String desc = String.valueOf(userInfo.get(PARAM_DESC));
		if (SUCCESS_CODE.equals(codeMes)) {
			//如果云信账户存在,则更新token,返给手机端
			String token = GeneratePwd.generatePwd();
			String accid = customerId + accidSuffix;
			Map<String, Object> tokenMap = neteaseUtils.updateUserToken(accid, token);
			if (SUCCESS_CODE.equals(String.valueOf(tokenMap.get(PARAM_CODE)))) {
				neteaseAccountMapper.updateByCustomerId(customerId, accid, token);
				saveAccount(neteaseAccountCondition, customerId, token, accid);
				result.setAccid(accid);
				result.setToken(token);
				result.setName(neteaseAccountCondition.getName());
				result.setIcon(neteaseAccountCondition.getIcon());
			} else {
				LOGGER.error("NeteaseServiceImpl ->createNeteaseAccount,云信账号更新失败，customerId={}", customerId);
				LOGGER.error("NeteaseServiceImpl ->createNeteaseAccount,云信账号更新失败，失败原因={}", tokenMap.get(PARAM_DESC));
				throw new BusinessException(BusinessCode.CODE_701302);
			}
		} else if (ERROR_CODE.equals(codeMes) || desc.indexOf(ERROR_MSG) > 0) {
			// 如果云信账号不存在，则创建
			String token = GeneratePwd.generatePwd();
			String accid = customerId + accidSuffix;
			Map<String, Object> createMap = neteaseUtils.createAccount(accid, token, neteaseAccountCondition.getName(), neteaseAccountCondition.getIcon());
			if (SUCCESS_CODE.equals(String.valueOf(createMap.get(PARAM_CODE)))) {
				saveAccount(neteaseAccountCondition, customerId, token, accid);
				result.setAccid(accid);
				result.setToken(token);
			} else {
				LOGGER.error("NeteaseServiceImpl ->createNeteaseAccount,创建网易云信账号失败 customerId={}", customerId);
				LOGGER.error("NeteaseServiceImpl ->createNeteaseAccount,创建网易云信账号失败，失败原因={}", createMap.get(PARAM_DESC));
				throw new BusinessException(BusinessCode.CODE_701303);
			}
		}
		return result;
	}

	@Override
	public void updateNeteaseAccount(NeteaseAccountCondition neteaseAccountCondition) {
		Long customerId = neteaseAccountCondition.getCustomerId();
		if (customerId == null) {
			LOGGER.error("消息服务 ->更新云信账号信息异常，NeteaseServiceImpl.updateNeteaseAccount(),参数错误，customerId是null");
			throw new BusinessException(BusinessCode.CODE_701301);
		}
		MessageNeteaseAccount account = neteaseAccountMapper.getNeteaseAccountByCustomerId(customerId);
		if (account == null) {
			LOGGER.error("消息服务 ->更新云信账号信息异常，NeteaseServiceImpl.updateNeteaseAccount(),云信用户不存在，customerId={}", customerId);
			throw new BusinessException(BusinessCode.CODE_701304);
		}
		//更新云信账户信息
		Map<String, Object> infoMap = neteaseUtils.updateUserInfo(account.getAccid(), account.getName(), account.getIcon());
		String codeMes = String.valueOf(infoMap.get(PARAM_CODE));
		if (SUCCESS_CODE.equals(codeMes)) {
			//更新云信用户记录
			account.setName(neteaseAccountCondition.getName());
			account.setMobile(neteaseAccountCondition.getMobile());
			account.setIcon(neteaseAccountCondition.getIcon());
			neteaseAccountMapper.updateByPrimaryKey(account);
		} else {
			LOGGER.error("NeteaseServiceImpl ->updateNeteaseAccount,云信账号更新失败，customerId={}", customerId);
			LOGGER.error("NeteaseServiceImpl ->updateNeteaseAccount,云信账号更新失败，失败原因={}", infoMap.get(PARAM_CODE));
			throw new BusinessException(BusinessCode.CODE_701302);
		}
	}

	@Override
	@MessageEnumConvertAnnotation
	public PagedList<NeteaseMsgVO> findNeteaseMsgBox(NeteaseMsgBoxCondition condition, Long storeId) {
		MessageNeteaseAccount neteaseAccount = neteaseAccountMapper.getNeteaseAccountByCustomerId(storeId);
		if (null == neteaseAccount || StringUtils.isBlank(neteaseAccount.getAccid())) {
			throw new BusinessException(BusinessCode.CODE_701101);
		}
		String accid = neteaseAccount.getAccid();
		PagedList<NeteaseMsgVO> pagedList = new PagedList<>();
		condition.setAccid(accid);
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		if (TIME_TYPE_TODAY.equals(condition.getTimeType())) {
			condition.setStartTime(formatter.format(currentDate));
		} else {
			condition.setEndTime(formatter.format(currentDate));
		}
		Page page = PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
		List<NeteaseMsgVO> neteaseMsgVOPage = neteaseHistoryMapper.selectVoByCondition(condition);
		pagedList.setPageNo(condition.getPageNo());
		pagedList.setPageSize(condition.getPageSize());
		pagedList.setTotalRows(page.getTotal());
		pagedList.setData(neteaseMsgVOPage);
		return pagedList;
	}

	@Override
	public Boolean modifyNeteaseMsgReadStatus(NeteaseMsgReadStatusCondition condition, Long storeId) {
		Boolean result = false;
		String accid;
		MessageNeteaseAccount neteaseAccount = neteaseAccountMapper.getNeteaseAccountByCustomerId(storeId);
		if (null == neteaseAccount || StringUtils.isBlank(neteaseAccount.getAccid())) {
			throw new BusinessException(BusinessCode.CODE_701101);
		}
		accid = neteaseAccount.getAccid();
		condition.setAccid(accid);
		/**
		 * 当全部已读的时候需要设置时间段
		 */
		if (condition.getAllRead() == 1) {
			Date currentDate = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			if (TIME_TYPE_TODAY.equals(condition.getTimeType())) {
				condition.setStartTime(formatter.format(currentDate));
			} else {
				condition.setEndTime(formatter.format(currentDate));
			}
		}
		int updateCount = neteaseHistoryMapper.updateReadStatusByCondition(condition);
		if (updateCount > 0) {
			result = true;
		}
		return result;
	}

	/**
     * 根据条件查询云信消息数量
	 * @param messageNeteaseCondition
     * @return
     */
	@Override
	public Integer getNeteaseMessageCount(MessageNeteaseCondition messageNeteaseCondition) {
		MessageNeteaseAccount neteaseAccount = neteaseAccountMapper.getNeteaseAccountByCustomerId(messageNeteaseCondition.getStoreId());
		if (null == neteaseAccount || StringUtils.isBlank(neteaseAccount.getAccid())) {
			throw new BusinessException(BusinessCode.CODE_701101);
		}
		String accid = neteaseAccount.getAccid();
		messageNeteaseCondition.setAccid(accid);
		Integer msgCount = neteaseHistoryMapper.getNeteaseMessageCount(messageNeteaseCondition);
		return msgCount;
	}


	/**
	 * 保存云信账户信息
	 *
	 * @param neteaseAccountCondition
	 * @param customerId
	 * @param token
	 * @param accid
	 * @return
	 */
	private MessageNeteaseAccount saveAccount(NeteaseAccountCondition neteaseAccountCondition, Long customerId, String token, String accid) {
		MessageNeteaseAccount account = new MessageNeteaseAccount();
		account.setCustomerId(customerId);
		account.setAccid(accid);
		account.setToken(token);
		account.setIcon(neteaseAccountCondition.getIcon());
		account.setMobile(neteaseAccountCondition.getMobile());
		account.setName(neteaseAccountCondition.getName());
		account.setCreated(new Date());
		neteaseAccountMapper.insert(account);
		return account;
	}

}
