/*
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.openrtb.json;

import static com.google.openrtb.json.OpenRtbJsonUtils.endArray;
import static com.google.openrtb.json.OpenRtbJsonUtils.endObject;
import static com.google.openrtb.json.OpenRtbJsonUtils.getCurrentName;
import static com.google.openrtb.json.OpenRtbJsonUtils.getIntBoolValue;
import static com.google.openrtb.json.OpenRtbJsonUtils.startArray;
import static com.google.openrtb.json.OpenRtbJsonUtils.startObject;

import com.google.common.io.Closeables;
import com.google.openrtb.OpenRtbNative.NativeRequest;
import com.google.openrtb.OpenRtbNative.NativeResponse;
import com.google.protobuf.ByteString;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Desserializes OpenRTB NativeRequest/NativeResponse messages from JSON.
 * <p>
 * This class is threadsafe.
 */
public class OpenRtbNativeJsonReader extends AbstractOpenRtbJsonReader {

  protected OpenRtbNativeJsonReader(OpenRtbJsonFactory factory) {
    super(factory);
  }

  /**
   * Desserializes a {@link NativeRequest} from a JSON string, provided as a {@link ByteString}.
   */
  public NativeRequest readNativeRequest(ByteString bs) throws IOException {
    return readNativeRequest(bs.newInput());
  }

  /**
   * Desserializes a {@link NativeRequest} from a JSON string, provided as a {@link CharSequence}.
   */
  public NativeRequest readNativeRequest(CharSequence chars) throws IOException {
    return readNativeRequest(new CharSequenceReader(chars));
  }

  /**
   * Desserializes a {@link NativeRequest} from JSON, streamed from a {@link Reader}.
   */
  public NativeRequest readNativeRequest(Reader reader) throws IOException {
    return readNativeRequest(factory().getJsonFactory().createParser(reader)).build();
  }

  /**
   * Desserializes a {@link NativeRequest} from JSON, streamed from an {@link InputStream}.
   */
  public NativeRequest readNativeRequest(InputStream is) throws IOException {
    try {
      return readNativeRequest(factory().getJsonFactory().createParser(is)).build();
    } finally {
      Closeables.closeQuietly(is);
    }
  }

  /**
   * Desserializes a {@link NativeRequest} from JSON, with a provided {@link JsonParser}
   * which allows several choices of input and encoding.
   */
  public final NativeRequest.Builder readNativeRequest(JsonParser par) throws IOException {
    NativeRequest.Builder req = NativeRequest.newBuilder();
    for (startObject(par); endObject(par); par.nextToken()) {
      String fieldName = getCurrentName(par);
      if (par.nextToken() != JsonToken.VALUE_NULL) {
        readNativeRequestField(par, req, fieldName);
      }
    }
    return req;
  }

  protected void readNativeRequestField(JsonParser par, NativeRequest.Builder req, String fieldName)
      throws IOException {
    switch (fieldName) {
      case "ver":
        req.setVer(par.getText());
        break;
      case "layout":
        req.setLayout(par.getIntValue());
        break;
      case "adunit":
        req.setAdunit(par.getIntValue());
        break;
      case "plcmtcnt":
        req.setPlcmtcnt(par.getIntValue());
        break;
      case "seq":
        req.setSeq(par.getIntValue());
        break;
      case "assets":
        for (startArray(par); endArray(par); par.nextToken()) {
          req.addAssets(readReqAsset(par));
        }
        break;
      case "ext":
        readExtensions(req, par, "NativeRequest");
        break;
    }
  }

  protected final NativeRequest.Asset.Builder readReqAsset(JsonParser par) throws IOException {
    NativeRequest.Asset.Builder asset = NativeRequest.Asset.newBuilder();
    for (startObject(par); endObject(par); par.nextToken()) {
      String fieldName = getCurrentName(par);
      if (par.nextToken() != JsonToken.VALUE_NULL) {
        readReqAssetField(par, asset, fieldName);
      }
    }
    return asset;
  }

  protected void readReqAssetField(
      JsonParser par, NativeRequest.Asset.Builder asset, String fieldName) throws IOException {
    switch (fieldName) {
      case "id":
        asset.setId(par.getIntValue());
        break;
      case "req":
        asset.setReq(getIntBoolValue(par));
        break;
      case "title":
        asset.setTitle(readReqTitle(par));
        break;
      case "img":
        asset.setImg(readReqImage(par));
        break;
      case "video":
        asset.setVideo(readReqVideo(par));
        break;
      case "data":
        asset.setData(readReqData(par));
        break;
      case "ext":
        readExtensions(asset, par, "NativeRequest.asset");
        break;
    }
  }

  protected final NativeRequest.Asset.Title.Builder readReqTitle(JsonParser par)
      throws IOException {
    NativeRequest.Asset.Title.Builder title = NativeRequest.Asset.Title.newBuilder();
    for (startObject(par); endObject(par); par.nextToken()) {
      String fieldName = getCurrentName(par);
      if (par.nextToken() != JsonToken.VALUE_NULL) {
        readReqTitleField(par, title, fieldName);
      }
    }
    return title;
  }

  protected void readReqTitleField(
      JsonParser par, NativeRequest.Asset.Title.Builder title, String fieldName)
      throws IOException {
    switch (fieldName) {
      case "len":
        title.setLen(par.getIntValue());
        break;
      case "ext":
        readExtensions(title, par, "NativeRequest.asset.title");
        break;
    }
  }

  protected final NativeRequest.Asset.Image.Builder readReqImage(JsonParser par)
      throws IOException {
    NativeRequest.Asset.Image.Builder req = NativeRequest.Asset.Image.newBuilder();
    for (startObject(par); endObject(par); par.nextToken()) {
      String fieldName = getCurrentName(par);
      if (par.nextToken() != JsonToken.VALUE_NULL) {
        readReqImageField(par, req, fieldName);
      }
    }
    return req;
  }

  protected void readReqImageField(
      JsonParser par, NativeRequest.Asset.Image.Builder image, String fieldName)
      throws IOException {
    switch (fieldName) {
      case "type":
        image.setType(par.getIntValue());
        break;
      case "w":
        image.setW(par.getIntValue());
        break;
      case "h":
        image.setH(par.getIntValue());
        break;
      case "wmin":
        image.setWmin(par.getIntValue());
        break;
      case "hmin":
        image.setHmin(par.getIntValue());
        break;
      case "mime":
        for (startArray(par); endArray(par); par.nextToken()) {
          image.addMime(par.getText());
        }
        break;
      case "ext":
        readExtensions(image, par, "NativeRequest.asset.img");
        break;
    }
  }

  protected final NativeRequest.Asset.Video.Builder readReqVideo(JsonParser par)
      throws IOException {
    NativeRequest.Asset.Video.Builder video = NativeRequest.Asset.Video.newBuilder();
    for (startObject(par); endObject(par); par.nextToken()) {
      String fieldName = getCurrentName(par);
      if (par.nextToken() != JsonToken.VALUE_NULL) {
        readReqVideoField(par, video, fieldName);
      }
    }
    return video;
  }

  protected void readReqVideoField(
      JsonParser par, NativeRequest.Asset.Video.Builder video, String fieldName)
      throws IOException {
    switch (fieldName) {
      case "mimes":
        for (startArray(par); endArray(par); par.nextToken()) {
          video.addMimes(par.getText());
        }
        break;
      case "minduration":
        video.setMinduration(par.getIntValue());
        break;
      case "maxduration":
        video.setMaxduration(par.getIntValue());
        break;
      case "protocols":
        for (startArray(par); endArray(par); par.nextToken()) {
          video.addProtocols(par.getIntValue());
        }
        break;
      case "ext":
        readExtensions(video, par, "NativeRequest.asset.video");
        break;
    }
  }

  protected final NativeRequest.Asset.Data.Builder readReqData(JsonParser par) throws IOException {
    NativeRequest.Asset.Data.Builder data = NativeRequest.Asset.Data.newBuilder();
    for (startObject(par); endObject(par); par.nextToken()) {
      String fieldName = getCurrentName(par);
      if (par.nextToken() != JsonToken.VALUE_NULL) {
        readReqDataField(par, data, fieldName);
      }
    }
    return data;
  }

  protected void readReqDataField(
      JsonParser par, NativeRequest.Asset.Data.Builder data, String fieldName) throws IOException {
    switch (fieldName) {
      case "type":
        data.setType(par.getIntValue());
        break;
      case "len":
        data.setLen(par.getIntValue());
        break;
      case "ext":
        readExtensions(data, par, "NativeRequest.asset.data");
        break;
    }
  }

  /**
   * Desserializes a {@link NativeResponse} from a JSON string, provided as a {@link ByteString}.
   */
  public NativeResponse readNativeResponse(ByteString bs) throws IOException {
    return readNativeResponse(bs.newInput());
  }

  /**
   * Desserializes a {@link NativeResponse} from a JSON string, provided as a {@link CharSequence}.
   */
  public NativeResponse readNativeResponse(CharSequence chars) throws IOException {
    return readNativeResponse(new CharSequenceReader(chars));
  }

  /**
   * Desserializes a {@link NativeResponse} from JSON, streamed from a {@link Reader}.
   */
  public NativeResponse readNativeResponse(Reader reader) throws IOException {
    return readNativeResponse(factory().getJsonFactory().createParser(reader)).build();
  }

  /**
   * Desserializes a {@link NativeResponse} from JSON, streamed from an {@link InputStream}.
   */
  public NativeResponse readNativeResponse(InputStream is) throws IOException {
    try {
      return readNativeResponse(factory().getJsonFactory().createParser(is)).build();
    } finally {
      Closeables.closeQuietly(is);
    }
  }

  /**
   * Desserializes a {@link NativeResponse} from JSON, with a provided {@link JsonParser}
   * which allows several choices of input and encoding.
   */
  public final NativeResponse.Builder readNativeResponse(JsonParser par) throws IOException {
    NativeResponse.Builder resp = NativeResponse.newBuilder();
    for (startObject(par); endObject(par); par.nextToken()) {
      String fieldName = getCurrentName(par);
      if (par.nextToken() != JsonToken.VALUE_NULL) {
        readNativeResponseField(par, resp, fieldName);
      }
    }
    return resp;
  }

  protected void readNativeResponseField(
      JsonParser par, NativeResponse.Builder resp, String fieldName) throws IOException {
    switch (fieldName) {
      case "ver":
        resp.setVer(par.getText());
        break;
      case "assets":
        for (startArray(par); endArray(par); par.nextToken()) {
          resp.addAssets(readRespAsset(par));
        }
        break;
      case "link":
        resp.setLink(readRespLink(par, "NativeResponse.link"));
        break;
      case "imptracker":
        for (startArray(par); endArray(par); par.nextToken()) {
          resp.addImptracker(par.getText());
        }
        break;
      case "jstracker":
        resp.setJstracker(par.getText());
        break;
      case "ext":
        readExtensions(resp, par, "NativeResponse");
        break;
    }
  }

  protected final NativeResponse.Asset.Builder readRespAsset(JsonParser par) throws IOException {
    NativeResponse.Asset.Builder asset = NativeResponse.Asset.newBuilder();
    for (startObject(par); endObject(par); par.nextToken()) {
      String fieldName = getCurrentName(par);
      if (par.nextToken() != JsonToken.VALUE_NULL) {
        readRespAssetField(par, asset, fieldName);
      }
    }
    return asset;
  }

  protected void readRespAssetField(
      JsonParser par, NativeResponse.Asset.Builder asset, String fieldName) throws IOException {
    switch (fieldName) {
      case "id":
        asset.setId(par.getIntValue());
        break;
      case "req":
        asset.setReq(getIntBoolValue(par));
        break;
      case "title":
        asset.setTitle(readRespTitle(par));
        break;
      case "img":
        asset.setImg(readRespImage(par));
        break;
      case "video":
        asset.setVideo(readRespVideo(par));
        break;
      case "data":
        asset.setData(readRespData(par));
        break;
      case "link":
        asset.setLink(readRespLink(par, "NativeResponse.asset.link"));
        break;
      case "ext":
        readExtensions(asset, par, "NativeResponse.asset");
        break;
    }
  }

  protected final NativeResponse.Asset.Title.Builder readRespTitle(JsonParser par)
      throws IOException {
    NativeResponse.Asset.Title.Builder title = NativeResponse.Asset.Title.newBuilder();
    for (startObject(par); endObject(par); par.nextToken()) {
      String fieldName = getCurrentName(par);
      if (par.nextToken() != JsonToken.VALUE_NULL) {
        readRespTitleField(par, title, fieldName);
      }
    }
    return title;
  }

  protected void readRespTitleField(
      JsonParser par, NativeResponse.Asset.Title.Builder title, String fieldName)
      throws IOException {
    switch (fieldName) {
      case "text":
        title.setText(par.getText());
        break;
      case "ext":
        readExtensions(title, par, "NativeResponse.asset.title");
        break;
    }
  }

  protected final NativeResponse.Asset.Image.Builder readRespImage(JsonParser par)
      throws IOException {
    NativeResponse.Asset.Image.Builder image = NativeResponse.Asset.Image.newBuilder();
    for (startObject(par); endObject(par); par.nextToken()) {
      String fieldName = getCurrentName(par);
      if (par.nextToken() != JsonToken.VALUE_NULL) {
        readRespImageField(par, image, fieldName);
      }
    }
    return image;
  }

  protected void readRespImageField(
      JsonParser par, NativeResponse.Asset.Image.Builder image, String fieldName)
      throws IOException {
    switch (fieldName) {
      case "url":
        image.setUrl(par.getText());
        break;
      case "w":
        image.setW(par.getIntValue());
        break;
      case "h":
        image.setH(par.getIntValue());
        break;
      case "ext":
        readExtensions(image, par, "NativeResponse.asset.img");
        break;
    }
  }

  protected final NativeResponse.Asset.Video.Builder readRespVideo(JsonParser par)
      throws IOException {
    NativeResponse.Asset.Video.Builder video = NativeResponse.Asset.Video.newBuilder();
    for (startObject(par); endObject(par); par.nextToken()) {
      String fieldName = getCurrentName(par);
      if (par.nextToken() != JsonToken.VALUE_NULL) {
        readRespVideoField(par, video, fieldName);
      }
    }
    return video;
  }

  protected void readRespVideoField(
      JsonParser par, NativeResponse.Asset.Video.Builder video, String fieldName)
      throws IOException {
    switch (fieldName) {
      case "vasttag":
        for (startArray(par); endArray(par); par.nextToken()) {
          video.addVasttag(par.getText());
        }
        break;
      case "ext":
        readExtensions(video, par, "NativeResponse.asset.video");
        break;
    }
  }

  protected final NativeResponse.Asset.Data.Builder readRespData(JsonParser par) throws IOException {
    NativeResponse.Asset.Data.Builder data = NativeResponse.Asset.Data.newBuilder();
    for (startObject(par); endObject(par); par.nextToken()) {
      String fieldName = getCurrentName(par);
      if (par.nextToken() != JsonToken.VALUE_NULL) {
        readRespDataField(par, data, fieldName);
      }
    }
    return data;
  }

  protected void readRespDataField(
      JsonParser par, NativeResponse.Asset.Data.Builder data, String fieldName) throws IOException {
    switch (fieldName) {
      case "label":
        data.setLabel(par.getText());
        break;
      case "value":
        data.setValue(par.getText());
        break;
      case "ext":
        readExtensions(data, par, "NativeResponse.asset.data");
        break;
    }
  }

  protected final NativeResponse.Link.Builder readRespLink(JsonParser par, String path) throws IOException {
    NativeResponse.Link.Builder link = NativeResponse.Link.newBuilder();
    for (startObject(par); endObject(par); par.nextToken()) {
      String fieldName = getCurrentName(par);
      if (par.nextToken() != JsonToken.VALUE_NULL) {
        readRespLinkField(par, path, link, fieldName);
      }
    }
    return link;
  }

  protected void readRespLinkField(
      JsonParser par, String path, NativeResponse.Link.Builder link, String fieldName) throws IOException {
    switch (fieldName) {
      case "url":
        link.setUrl(par.getText());
        break;
      case "clktrck":
        for (startArray(par); endArray(par); par.nextToken()) {
          link.addClktrck(par.getText());
        }
        break;
      case "fallback":
        link.setFallback(par.getText());
        break;
      case "ext":
        readExtensions(link, par, path);
        break;
    }
  }
}
