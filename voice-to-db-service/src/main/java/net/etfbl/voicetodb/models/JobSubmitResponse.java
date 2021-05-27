package net.etfbl.voicetodb.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {@code JobSubmitResponse} describes confirmation of accepted request. Request id matches job id, connecting stored
 * files, result and the entire process.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobSubmitResponse {

   public static JobSubmitResponse EMPTY = new JobSubmitResponse(null);

   private String requestId;

}
