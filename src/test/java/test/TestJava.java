package test;

import cn.hutool.core.codec.Base58;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.gitee.ixtf.J;
import java.nio.file.Path;

public class TestJava {
  public static void main(String[] args) {
    final var path1 = Path.of("/Users/ixtf/Desktop/gdc.xlsx");
    final var bytes1 = DigestUtil.sha256(path1.toFile());
    System.out.println(Base58.encode(bytes1));
    final var path2 = Path.of("/Users/ixtf/Desktop/WechatIMG2.jpeg");
    final var bytes2 = DigestUtil.sha256(path2.toFile());
    System.out.println(Base58.encode(bytes2));
    System.out.println(J.base58(Base58.encode(bytes1), Base58.encode(bytes2)));
    final var bytes = ArrayUtil.addAll(bytes1, bytes2);
    System.out.println(Base58.encode(bytes));

    System.out.println(J.base58("1", "2"));
    System.out.println(J.base58("1", "2", "3"));
    System.out.println(J.base58("1", "2", "12"));
    System.out.println(J.base58("1", "2", "3"));
  }

  public String dd(String... ss) {
    return null;
  }
}
