package net.etfbl.voicetodb.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponse {

   public static final ResultResponse EMPTY = new ResultResponse(null);

   private String text;
}
