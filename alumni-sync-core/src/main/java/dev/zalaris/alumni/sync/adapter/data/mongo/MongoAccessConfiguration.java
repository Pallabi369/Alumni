package dev.zalaris.alumni.sync.adapter.data.mongo;

import com.bol.config.EncryptAutoConfiguration;
import com.bol.crypt.CryptVault;
import dev.zalaris.alumni.common.encrypt.MongoEncryptConfig;
import dev.zalaris.alumni.sync.app.employee.port.outbound.EmployeeRepository;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
@AutoConfigureAfter(MongoAutoConfiguration.class)
@AutoConfigureBefore(EncryptAutoConfiguration.class)
@Import(MongoEncryptConfig.class)
class MongoAccessConfiguration {

  @Bean
  @ConditionalOnMissingBean(EmployeeRepository.class)
  EmployeeMongoAccess updateEmployeeMongoAccess(MongoTemplate mongoTemplate) {
    return new EmployeeMongoAccess(mongoTemplate);
  }

  @Bean
  @ConditionalOnMissingBean(PayslipFileMongoAccess.class)
  PayslipFileMongoAccess storePayslipFileMongoAccess(GridFsTemplate gridFsTemplate, CryptVault cryptVault){
    return new PayslipFileMongoAccess(gridFsTemplate, cryptVault);
  }
}
