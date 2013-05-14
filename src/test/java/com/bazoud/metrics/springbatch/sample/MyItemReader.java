package com.bazoud.metrics.springbatch.sample;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

/**
 * @author @obazoud (Olivier Bazoud)
 */
@Component
public class MyItemReader implements ItemReader<String> {
  private String[] data = { "Hello", "World", "!" };
  private int index = 0;

  @Override
  public String read() throws Exception {
    if (index >= data.length) return null;
    return data[index++];
  }
}
