package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.product.condition.ProductCondition;
import com.winhxd.b2c.common.domain.product.enums.SearchSkuCodeEnum;
import com.winhxd.b2c.common.domain.product.vo.BrandVO;
import com.winhxd.b2c.common.domain.product.vo.ProductVO;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.order.OrderServiceApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderServiceApplication.class)
public class ProductServiceTest {

    
    @Autowired
    private ProductServiceClient productServiceClient;
    
    @Test
    public void testGetBrands() {
    	List<String> skuCodes = new ArrayList<String>();
    	skuCodes.add("SanQuanJinLongYu858012");
    	ResponseResult<List<BrandVO>> s = productServiceClient.getBrandInfoBySku(skuCodes);
    	System.out.println(s);
    }
    
    @Test
    public void testGetProducts() {
    	ProductCondition condition = new ProductCondition();
    	List<String> productSkus = new ArrayList<String>();
    	productSkus.add("SanQuanJinLongYu858094");
    	productSkus.add("SanQuanJinLongYu858012");
    	productSkus.add("JianPai85758998");
    	productSkus.add("LiBaiXiangZao12");
    	condition.setProductSkus(productSkus);
    	condition.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
    	ResponseResult<List<ProductVO>> s = productServiceClient.getProducts(condition);
    	System.out.println(s);
    }
}
