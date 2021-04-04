package net.etfbl.voicetodb.models;

import lombok.Data;

@Data
public class FileUploadResponse {

   public static FileUploadResponse EMPTY = new FileUploadResponse(null);

   private final String id;

}
