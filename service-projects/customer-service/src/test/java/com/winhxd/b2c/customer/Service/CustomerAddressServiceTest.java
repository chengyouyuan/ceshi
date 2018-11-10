package com.winhxd.b2c.customer.Service;

import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressCondition;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressQueryCondition;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressSelectCondition;
import com.winhxd.b2c.common.domain.customer.model.CustomerAddress;
import com.winhxd.b2c.common.domain.customer.vo.CustomerAddressVO;
import com.winhxd.b2c.customer.dao.CustomerAddressMapper;
import com.winhxd.b2c.customer.service.CustomerAddressService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * @author: sunwenwu
 * @Date: 2018/11/8 15：51
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerAddressServiceTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CustomerAddressService customerAddressService;
    @Autowired
    private CustomerAddressMapper customerAddressMapper;



    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    /**
     * 测试用户收货地址保存功能
     */
    @Test
    public void customerSaveAddressTest() throws Exception {
        CustomerAddressCondition condition = new CustomerAddressCondition();
        condition.setContacterDetailAddress("东升科技园");
        condition.setContacterProvince("北京市");
        condition.setContacterCity("海淀区");
        condition.setContacterCounty("西三旗");
        condition.setContacterMobile("13800001111");
        condition.setContacterName("孙悟空之间测试");
        condition.setLabelId(3L);
        CustomerUser customerUser = new CustomerUser();
        customerUser.setCustomerId(20L);

        int insert = customerAddressService.saveCustomerAddress(condition,customerUser);
        System.out.println("customerSaveAddressTest----------------result:"+insert);
    }


    @Test
    public void upateCustomerAddress() {
        CustomerAddressCondition condition = new CustomerAddressCondition();
//       condition.setContacterDetailAddress("东升科技园3");
//        condition.setContacterRegion("北京市海淀区西三旗3");
//        condition.setContacterMobile("13800003333");
        condition.setContacterName("我就tetst试试");
//        condition.setLabelId(23L);
        CustomerUser customerUser = new CustomerUser();
        customerUser.setCustomerId(20L);
        condition.setId(3L);
        condition.setDefaultAddress(true);


        customerAddressService.updateCustomerAddress(condition,customerUser);
    }

    @Test
    public void selectCustomerAddress() {
        CustomerAddressQueryCondition condition = new CustomerAddressQueryCondition();

        condition.setContacterMobile("18812345678");

        condition.setId(2L);
//        condition.setDefaultAddress(true);


        List<CustomerAddressVO> customerAddressVOS = customerAddressMapper.selectCustomerAddressByCondtion(condition);
        System.out.println(customerAddressVOS);
    }

    @Test
    public void selectCustomerDefaultAddress() {
        CustomerUser cu = new CustomerUser();
        cu.setCustomerId(20l);

        CustomerAddressVO customerAddressVOS = customerAddressService.selectDefaultCustomerAddress(cu);
        System.out.println(customerAddressVOS);
    }
}
