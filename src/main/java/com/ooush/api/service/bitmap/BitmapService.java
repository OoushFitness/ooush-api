package com.ooush.api.service.bitmap;

import java.util.List;
import java.util.Map;

import com.ooush.api.dto.response.BitmapSearchOption;

public interface BitmapService {

    Map<String, List<BitmapSearchOption>> fetchSearchOptions();

}
