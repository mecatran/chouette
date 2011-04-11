package fr.certu.chouette.model.neptune.type;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * coordinates when an alternative projection referential is available
 * 
 * @author michel
 *
 */
public class ProjectedPoint 
{
	/**
	 * x coordinate
	 */
	@Getter @Setter private BigDecimal x;
	/**
	 * y coordinate
	 */
	@Getter @Setter private BigDecimal y;
	/**
	 * projection system name (f.e. : epgs:27578)
	 */
	@Getter @Setter private String projectionType;
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("x=").append(x).append(" y=").append(y).append(" projection=").append(projectionType);
		return sb.toString();
	}
}
