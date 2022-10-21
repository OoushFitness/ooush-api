package com.ooush.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.dto.response.OoushResponseMap;
import com.ooush.api.service.bitmap.BitmapServiceImpl;

@RestController
@RequestMapping(value = { "/bitmap" })
public class BitmapController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitmapController.class);

    @Autowired
    BitmapServiceImpl bitmapService;

    @GetMapping(value = "/fetch-search-options")
    public OoushResponseEntity fetchSearchOptions() {
        LOGGER.info("Resource /bitmap/fetch-search-options GET called");
        return new OoushResponseEntity(OoushResponseMap.createResponseMap(bitmapService.fetchSearchOptions()).construct());
    }

}
