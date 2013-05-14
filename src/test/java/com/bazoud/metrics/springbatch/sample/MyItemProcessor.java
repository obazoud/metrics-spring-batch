package com.bazoud.metrics.springbatch.sample;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * @author @obazoud (Olivier Bazoud)
 */
@Component
public class MyItemProcessor implements ItemProcessor<String, String> {
  @Override
  public String process(String item) throws Exception {
    return item;
  }
}
