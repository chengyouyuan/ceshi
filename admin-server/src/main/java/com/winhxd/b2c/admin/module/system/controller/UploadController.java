package com.winhxd.b2c.admin.module.system.controller;


import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.common.model.BaseImageFile;
import com.winhxd.b2c.common.util.ImageUploadUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(tags = "文件上传管理")
@RestController
@RequestMapping("/upload")
public class UploadController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private ImageUploadUtil imageUploadUtil;

    @ApiOperation("上传图片，如果存在则覆盖")
    @PostMapping(value = "/picture",consumes = "multipart/*",headers = "content-type=multipart/form-data")
    public ResponseResult<BaseImageFile> findRegionList(@RequestParam("file") MultipartFile file) throws IOException,Exception {
        ResponseResult<BaseImageFile> result = new ResponseResult<>();
        BaseImageFile baseImageFile = imageUploadUtil.uploadImage(file, null);
        result.setData(baseImageFile);
        return result;
    }
}
