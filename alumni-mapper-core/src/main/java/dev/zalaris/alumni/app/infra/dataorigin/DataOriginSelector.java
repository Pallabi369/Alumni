package dev.zalaris.alumni.app.infra.dataorigin;

/**
 * The implementers define from where data should be retrieved.
 */
public interface DataOriginSelector {

  DataOrigin select();

}
