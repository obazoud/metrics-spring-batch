package com.bazoud.metrics.springbatch.sample;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author @obazoud (Olivier Bazoud)
 */
@Component
public class MyItemWriter implements ItemWriter<String> {

  @Override
  public void write(List<? extends String> items) throws Exception {
  }
}
