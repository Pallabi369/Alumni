package dev.zalaris.alumni.app.infra.dataorigin;

/**
 * This implementation forces only alumni databases to be used to retrieve the requested data.
 */
class AlumniDataOriginSelector implements DataOriginSelector {

  @Override
  public DataOrigin select() {
    return DataOrigin.ALUMNI;
  }

}
