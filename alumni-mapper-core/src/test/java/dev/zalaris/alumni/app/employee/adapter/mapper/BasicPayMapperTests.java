package dev.zalaris.alumni.app.employee.adapter.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zalaris.api.nonsap.DtP0008;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasicPayMapperTests {


  @Test
  void whenInfotypeIsEmpty_expectEmptyList() {
    var service = new BasicPayMapperImpl();
    var data = new DtP0008();

    var result = service.mapToWages(data);
    assertTrue(result.isEmpty());
  }

  @Test
  void whenInfotypeIsNull_expectEmptyList() {
    var service = new BasicPayMapperImpl();
    var result = service.mapToWages(null);
    assertTrue(result.isEmpty());
  }

  @Test
  void whenInfotypeHasOneEntry_expectOneElement() throws IOException {
    var service = new BasicPayMapperImpl();
    var json = """
      {"AEDTM":"20210930","ANCUR":"NOK","ANCUR_TEXT":"Norwegian Krone","ANSAL":"0.0",
      "ANZ01":"0.0","ANZ02":"0.0","ANZ03":"0.0","ANZ04":"0.0","ANZ05":"0.0","ANZ06":"0.0","ANZ07":"0.0","ANZ08":"0.0","ANZ09":"0.0","ANZ10":"0.0","ANZ11":"0.0","ANZ12":"0.0","ANZ13":"0.0","ANZ14":"0.0","ANZ15":"0.0","ANZ16":"0.0","ANZ17":"0.0","ANZ18":"0.0","ANZ19":"0.0","ANZ20":"0.0","ANZ21":"0.0","ANZ22":"0.0","ANZ23":"0.0","ANZ24":"0.0","ANZ25":"0.0","ANZ26":"0.0","ANZ27":"0.0","ANZ28":"0.0","ANZ29":"0.0","ANZ30":"0.0","ANZ31":"0.0","ANZ32":"0.0","ANZ33":"0.0","ANZ34":"0.0","ANZ35":"0.0","ANZ36":"0.0","ANZ37":"0.0","ANZ38":"0.0","ANZ39":"0.0","ANZ40":"0.0",
      "BEGDA":"20210701",
      "BET01":"125460.0","BET02":"0.0","BET03":"0.0","BET04":"0.0","BET05":"0.0","BET06":"0.0","BET07":"0.0","BET08":"0.0","BET09":"0.0","BET10":"0.0","BET11":"0.0","BET12":"0.0","BET13":"0.0","BET14":"0.0","BET15":"0.0","BET16":"0.0","BET17":"0.0","BET18":"0.0","BET19":"0.0","BET20":"0.0","BET21":"0.0","BET22":"0.0","BET23":"0.0","BET24":"0.0","BET25":"0.0","BET26":"0.0","BET27":"0.0","BET28":"0.0","BET29":"0.0","BET30":"0.0","BET31":"0.0","BET32":"0.0","BET33":"0.0","BET34":"0.0","BET35":"0.0","BET36":"0.0","BET37":"0.0","BET38":"0.0","BET39":"0.0","BET40":"0.0",
      "BSGRD":"100.0","CPIND":"T","CPIND_TEXT":"Pay scale structure","DIVGV":"162.5","ENDDA":"99991231","FLAGA_TEXT":"Flag is Not Set","ITXEX_TEXT":"No",
      "LGA01":"1000","LGA01_TEXT":"Monthly salary",
      "OPK01_TEXT":"Payment","OPK02_TEXT":"Payment","OPK03_TEXT":"Payment","OPK04_TEXT":"Payment","OPK05_TEXT":"Payment","OPK06_TEXT":"Payment","OPK07_TEXT":"Payment","OPK08_TEXT":"Payment","OPK09_TEXT":"Payment","OPK10_TEXT":"Payment","OPK11_TEXT":"Payment","OPK12_TEXT":"Payment","OPK13_TEXT":"Payment","OPK14_TEXT":"Payment","OPK15_TEXT":"Payment","OPK16_TEXT":"Payment","OPK17_TEXT":"Payment","OPK18_TEXT":"Payment","OPK19_TEXT":"Payment","OPK20_TEXT":"Payment","OPK21_TEXT":"Payment","OPK22_TEXT":"Payment","OPK23_TEXT":"Payment","OPK24_TEXT":"Payment","OPK25_TEXT":"Payment","OPK26_TEXT":"Payment","OPK27_TEXT":"Payment","OPK28_TEXT":"Payment","OPK29_TEXT":"Payment","OPK30_TEXT":"Payment","OPK31_TEXT":"Payment","OPK32_TEXT":"Payment","OPK33_TEXT":"Payment","OPK34_TEXT":"Payment","OPK35_TEXT":"Payment","OPK36_TEXT":"Payment","OPK37_TEXT":"Payment","OPK38_TEXT":"Payment","OPK39_TEXT":"Payment","OPK40_TEXT":"Payment",
      "ORDEX_TEXT":"No","PERNR":"20000001","REFEX_TEXT":"No","SEQNR":"000","STVOR":"00000000","SUBTY":"0","SUBTY_TEXT":"Basic contract","TRFAR":"01","TRFAR_TEXT":"Individual salary","TRFGB":"01","TRFGB_TEXT":"Monthly salary","TRFGR":"INDIV","UNAME":"HIHJ","VGLSV":"00000000","WAERS":"NOK","WAERS_TEXT":"Norwegian Krone"}
      """;
    var data = new ObjectMapper().readValue(json.getBytes(StandardCharsets.UTF_8), DtP0008.class);
    var result = service.mapToWages(data);
    assertTrue(!result.isEmpty() && result.size() == 1 );

    var paywage = result.get(0);
    assertEquals(data.getLga01Text(), paywage.type());
    assertEquals(data.getBet01(), paywage.amount());
    assertEquals(data.getWaersText(), paywage.unit());
  }

  @Test
  void whenInfotypeHasManyEntries_expectManyElement() {
    var service = new BasicPayMapperImpl();
    var data = new DtP0008();
    data.setWaersText("Cows");

    // all data
    data.setLga02("2000");
    data.setLga02Text("Apples");
    data.setBet02("10");

    // missing lga text (lga code used instead)
    data.setLga03("3000");
    data.setBet03("500");

    // data in random raw
    data.setLga40("4000");
    data.setBet40("0.5");

    var result = service.mapToWages(data);
    assertTrue(!result.isEmpty() && result.size() == 3 );

    var paywage2 = result.get(0);
    assertEquals(data.getLga02Text(), paywage2.type());
    assertEquals(data.getBet02(), paywage2.amount());
    assertEquals(data.getWaersText(), paywage2.unit());

    var paywage3 = result.get(1);
    assertEquals(data.getLga03(), paywage3.type());
    assertEquals(data.getBet03(), paywage3.amount());
    assertEquals(data.getWaersText(), paywage3.unit());

    var paywage4 = result.get(2);
    assertEquals(data.getLga40(), paywage4.type());
    assertEquals(data.getBet40(), paywage4.amount());
    assertEquals(data.getWaersText(), paywage4.unit());

  }

}
