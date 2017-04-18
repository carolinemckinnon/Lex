package gnu.trove.impl.unmodifiable;

import gnu.trove.set.TCharSet;
import java.io.Serializable;














































public class TUnmodifiableCharSet
  extends TUnmodifiableCharCollection
  implements TCharSet, Serializable
{
  private static final long serialVersionUID = -9215047833775013803L;
  
  public TUnmodifiableCharSet(TCharSet s) { super(s); }
  public boolean equals(Object o) { return (o == this) || (c.equals(o)); }
  public int hashCode() { return c.hashCode(); }
}
