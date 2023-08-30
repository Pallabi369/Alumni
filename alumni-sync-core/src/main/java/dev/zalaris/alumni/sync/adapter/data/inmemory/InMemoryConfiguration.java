package dev.zalaris.alumni.sync.adapter.data.inmemory;

import dev.zalaris.alumni.common.domain.Employee;
import dev.zalaris.alumni.sync.app.employee.port.outbound.EmployeeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Profile("inmemory")
class InMemoryConfiguration {

  @Bean
  Map<String, Employee> inMemoryStore() {
    return new ConcurrentHashMap<>();
  }

  @Bean
  EmployeeRepository updateEmployeeDataAccess(Map<String, Employee> inMemoryStore) {
    return new EmployeeInMemoryAccess(inMemoryStore);
  }

}
