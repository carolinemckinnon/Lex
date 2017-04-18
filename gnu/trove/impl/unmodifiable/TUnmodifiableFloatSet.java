package gnu.trove.impl.unmodifiable;

import gnu.trove.set.TFloatSet;
import java.io.Serializable;














































public class TUnmodifiableFloatSet
  extends TUnmodifiableFloatCollection
  implements TFloatSet, Serializable
{
  private static final long serialVersionUID = -9215047833775013803L;
  
  public TUnmodifiableFloatSet(TFloatSet s) { super(s); }
  public boolean equals(Object o) { return (o == this) || (c.equals(o)); }
  public int hashCode() { return c.hashCode(); }
}
