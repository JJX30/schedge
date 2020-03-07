package actions;

import static database.epochs.LatestCompleteEpoch.getLatestEpoch;

import database.GetConnection;
import database.epochs.CleanEpoch;
import nyu.Term;
import search.GetResources;
import utils.Utils;

public class CleanData {

  public static void cleanData() {

    Term current = Term.getCurrentTerm();
    Term prev = current.prevTerm();
    Term next = current.nextTerm();
    Term nextNext = current.nextTerm();

    Integer maxDeletableEpoch = GetConnection.withContextReturning(context -> {
      Integer min = Integer.MAX_VALUE;
      Integer cur = null;

      if (min == null ||
          ((cur = getLatestEpoch(context, prev)) != null && cur < min)) {
        min = cur;
      }

      if (min == null ||
          ((cur = getLatestEpoch(context, current)) != null && cur < min)) {
        min = cur;
      }

      if (min == null ||
          ((cur = getLatestEpoch(context, next)) != null && cur < min)) {
        min = cur;
      }

      if (min == null ||
          ((cur = getLatestEpoch(context, nextNext)) != null && cur < min)) {
        min = cur;
      }

      return min - 1;
    });

    if (maxDeletableEpoch == null)
      return;

    GetConnection.withContext(
        context -> CleanEpoch.cleanEpochsUpTo(context, maxDeletableEpoch));

    int currentEpoch = maxDeletableEpoch;
    while (Utils.deleteFile(GetResources.getIndexFileForEpoch(currentEpoch--)))
      ;
  }
}
