package edu.ucla.sspace.tri;

import edu.ucla.sspace.util.TimeSpan;
import java.util.Properties;











































































































































































































public class FixedDurationTemporalRandomIndexing
  extends OrderedTemporalRandomIndexing
{
  public static final TimeSpan DEFAULT_SEMANTIC_PARTITION_DURATION = new TimeSpan(0, 1, 0, 0, 0);
  





  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.tri.FixedDurationTemporalRandomIndexing";
  




  public static final String SEMANTIC_PARTITION_DURATION_PROPERTY = "edu.ucla.sspace.tri.FixedDurationTemporalRandomIndexing.partitionDuration";
  




  private final TimeSpan partitionDuration;
  





  public FixedDurationTemporalRandomIndexing()
  {
    this(System.getProperties());
  }
  








  public FixedDurationTemporalRandomIndexing(Properties props)
  {
    super(props);
    
    String timeSpanProp = 
      props.getProperty("edu.ucla.sspace.tri.FixedDurationTemporalRandomIndexing.partitionDuration");
    
    partitionDuration = (timeSpanProp == null ? 
      DEFAULT_SEMANTIC_PARTITION_DURATION : 
      new TimeSpan(timeSpanProp));
  }
  


  public String getSpaceName()
  {
    return "edu.ucla.sspace.tri.FixedDurationTemporalRandomIndexing-" + partitionDuration + "-" + getVectorLength();
  }
  








  protected boolean shouldPartitionSpace(long timeStamp)
  {
    return !partitionDuration.insideRange(startTime.longValue(), timeStamp);
  }
}
