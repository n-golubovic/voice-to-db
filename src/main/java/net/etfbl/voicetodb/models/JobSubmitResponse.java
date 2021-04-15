package net.etfbl.voicetodb.models;

import lombok.Data;

/**
 * {@code JobSubmitResponse} describes confirmation of accepted request. Request id matches job id, connecting stored
 * files, result and the entire process.
 */
@Data
public class JobSubmitResponse {

   public static JobSubmitResponse EMPTY = new JobSubmitResponse(null);

   private final String requestId;

}
