package render;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 * @author Michael Muré <batolettre@gmail.com>
 * @version 1.0 initial version
 */
public class ColorRescaleFilter extends Filter
{
	private ColorRGBA offsets = new ColorRGBA(0f, 0f, 0f, 0f);
	private ColorRGBA gains = new ColorRGBA(1f, 1f, 1f, 1f);

	public ColorRescaleFilter()
	{
		super("ColorRescaleFilter");
	}

	public ColorRescaleFilter(float offset, float gain)
	{
		this();
		this.setOffset(offset);
		this.setGain(gain);
	}

	@Override
	protected Material getMaterial()
	{
		return material;
	}

	@Override
	protected void initFilter(AssetManager manager,
			RenderManager renderManager, ViewPort vp, int w, int h)
	{
		material = new Material(manager, "render/ColorRescale.j3md");
		material.setColor("offsets", offsets);
		material.setColor("gains", gains);
	}

	public ColorRGBA getOffsets()
	{
		return offsets;
	}

	/** Set the same offset for RGB, don't affect alpha. */
	public void setOffset(float offset) {
		setOffsets(new ColorRGBA(offset, offset, offset, 0));
	}

	public void setOffsets(ColorRGBA offsets)
	{
		if (material != null)
		{
			material.setColor("offsets", offsets);
		}
		this.offsets = offsets;
	}

	public ColorRGBA getGains()
	{
		return gains;
	}

	/** Set the same gain for RGB, don't affect alpha. */
	public void setGain(float gain) {
		setGains(new ColorRGBA(gain, gain, gain, 1));
	}

	public void setGains(ColorRGBA gains)
	{
		if (material != null)
		{
			material.setColor("gains", gains);
		}
		this.gains = gains;
	}
}