package graph;

import cn.hutool.core.util.ArrayUtil;
import java.util.*;

public class Graph {
  private Map<String, Vertex> vertexMap = new HashMap<>();
  private Collection<Edge> edges;

  public Vertex addVertex(String id) {
    return vertexMap.put(id, new Vertex());
  }

  public Collection<Vertex> vertices(String... ids) {
    if (ArrayUtil.isEmpty(ids)) {
      return vertexMap.values();
    }
    return Arrays.stream(ids).map(vertexMap::get).filter(Objects::nonNull).toList();
  }
}
