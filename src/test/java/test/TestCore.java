package test;

import static org.junit.jupiter.api.Assertions.*;

import com.gitee.ixtf.EntityDTO;
import com.gitee.ixtf.J;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class TestCore {
  @Test
  void testLocalIp() {
    assertFalse(J.localIp().isBlank());
  }

  @Test
  void testEntityDTO() {
    final var emptyJson = """
              {"id":""}
            """;
    final var emptyDto = J.readJson(emptyJson, EntityDTO.class);
    assertTrue(emptyDto.getId().isBlank());
    assertThrows(
        ConstraintViolationException.class, () -> J.checkAndGetCommand(emptyJson, EntityDTO.class));

    final var json = """
              {"id":"id"}
            """;
    final var dto = J.checkAndGetCommand(json, EntityDTO.class);
    assertEquals("id", dto.getId());
  }
}
