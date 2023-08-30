package dev.zalaris.alumni.sync.config;

import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashSet;
import java.util.Set;

public class CompositeAutoConfigurationImportSelector extends AutoConfigurationImportSelector {

  private static final String PROPERTY_SUFFIX = ".autoconfigure.exclude";
  private static final String INMEMORY = "inmemory";
  private static final String INMEMORY_PROPERTY = INMEMORY + PROPERTY_SUFFIX;
  private static final String MOCKBUS = "mockbus";
  private static final String MOCKBUS_PROPERTY = MOCKBUS + PROPERTY_SUFFIX;

  @Override
  protected Set<String> getExclusions(AnnotationMetadata metadata, AnnotationAttributes attributes) {
    Set<String> exclusions = super.getExclusions(metadata, attributes);
    var profiles = Set.of(getEnvironment().getActiveProfiles());
    if (profiles.contains(INMEMORY)) {
      exclusions.addAll(readExclusions(INMEMORY_PROPERTY));
    }

    if (profiles.contains(MOCKBUS)) {
      exclusions.addAll(readExclusions(MOCKBUS_PROPERTY));
    }
    return exclusions;
  }

  private Set<String> readExclusions(String propertyName) {
    Set<String> result = new HashSet<>();
    int i = 0;
    for (var indexedPropName = propertyName + "[" + i + "]";
         getEnvironment().containsProperty(indexedPropName);
         indexedPropName = propertyName + "[" + ++i + "]") {
      result.add(getEnvironment().getProperty(indexedPropName));
    }
    return result;
  }

}
