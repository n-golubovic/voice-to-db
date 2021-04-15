package net.etfbl.voicetodb.models;

import lombok.Data;

@Data
public class JobSubmitResponse {

   public static JobSubmitResponse EMPTY = new JobSubmitResponse(null);

   private final String requestId;

}
