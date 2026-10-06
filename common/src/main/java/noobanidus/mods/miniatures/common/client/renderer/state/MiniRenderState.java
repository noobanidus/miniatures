package noobanidus.mods.miniatures.common.client.renderer.state;

import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.entity.player.PlayerSkin;

public class MiniRenderState extends HumanoidRenderState {
  public PlayerSkin skin = DefaultPlayerSkin.getDefaultSkin();
  public boolean isMaxi = false;
  public boolean isPowered = false;
  public int noobVariant = -1;
  public double bbHeight;
  public int arrowCount;
  public int stingerCount;
  public int id;
  public final ItemStackRenderState heldOnHead = new ItemStackRenderState();
}
