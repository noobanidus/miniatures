package noobanidus.mods.miniatures.client.renderer.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.ResourceLocation;
import noobanidus.mods.miniatures.client.ModelHolder;
import noobanidus.mods.miniatures.client.model.MiniMeModel;
import noobanidus.mods.miniatures.client.renderer.layers.ArrowRenderTypeLayer;
import noobanidus.mods.miniatures.client.renderer.layers.BeeStingerRenderTypeLayer;
import noobanidus.mods.miniatures.entity.MiniMeEntity;
import noobanidus.mods.miniatures.setup.ClientSetup;
import org.apache.commons.lang3.StringUtils;

public class MaxiMeRenderer extends HumanoidMobRenderer<MiniMeEntity, MiniMeModel<MiniMeEntity>> {
  private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/player/wide/steve.png");
  public boolean isSlim = false;

  public MaxiMeRenderer(EntityRendererProvider.Context context) {
    super(context, new MiniMeModel<>(context.bakeLayer(ClientSetup.MINI_ME), false), 0.5f);
    ModelHolder.init(context);
    this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    this.addLayer(new ArrowRenderTypeLayer<>(context, this));
    this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
    this.addLayer(new ElytraLayer<>(this, context.getModelSet()));
    this.addLayer(new BeeStingerRenderTypeLayer<>(this));
    this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ClientSetup.MINI_ME_ARMOR)), new HumanoidModel<>(context.bakeLayer(ClientSetup.MINI_ME_ARMOR)), context.getModelManager()));
  }

  @Override
  public ResourceLocation getTextureLocation(MiniMeEntity entity) {
    return entity.getGameProfile()
            .map(this::getSkin)
            .orElse(TEXTURE_STEVE);
  }

  private ResourceLocation getSkin(GameProfile gameProfile) {
    if (gameProfile.getId() == null || !StringUtils.isNotBlank(gameProfile.getName())) {
      //Miniatures.LOG.error("STEVE: GameProfile incomplete for " + gameProfile);
      return TEXTURE_STEVE;
    } else {
      SkinManager skinmanager = Minecraft.getInstance().getSkinManager();
      return skinmanager.getInsecureSkin(gameProfile).texture();
    }
  }

  @Override
  public void render(MiniMeEntity miniMeEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
    SkinManager skinmanager = Minecraft.getInstance().getSkinManager();
    if (miniMeEntity.getGameProfile().isPresent()) {
      if (isSlim != skinmanager.getInsecureSkin(miniMeEntity.getGameProfile().get()).model().id().equals("slim"))
        isSlim = !isSlim;
    }
    this.model = isSlim ? ModelHolder.miniMeSlim : ModelHolder.miniMe;
    int noob = miniMeEntity.getNoobVariant();
/*    if (noob == 3) {
      packedLightIn = 15728880;
      this.model = ModelHolder.miniMeGhost;
      if (miniMeEntity.isSlim() && this.model != ModelHolder.miniMeGhostSlim) {
        this.model = ModelHolder.miniMeGhostSlim;
      }
    } else if (noob == 4) {
      packedLightIn = 15728880;
      this.model = ModelHolder.miniMeGlow;
      if (miniMeEntity.isSlim() && this.model != ModelHolder.miniMeGlowSlim) {
        this.model = ModelHolder.miniMeGlowSlim;
      }
    }*/
    super.render(miniMeEntity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
  }

  protected void scale(MiniMeEntity miniMeEntity, PoseStack poseStack, float partialTickTime) {
    poseStack.scale(3.5375F, 3.5375F, 3.5375F);
  }
}
