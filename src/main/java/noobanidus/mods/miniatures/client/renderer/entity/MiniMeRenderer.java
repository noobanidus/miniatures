package noobanidus.mods.miniatures.client.renderer.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.ResourceLocation;
import noobanidus.mods.miniatures.client.ModelHolder;
import noobanidus.mods.miniatures.client.model.MiniMeModel;
import noobanidus.mods.miniatures.client.renderer.layers.ArrowRenderTypeLayer;
import noobanidus.mods.miniatures.client.renderer.layers.BeeStingerRenderTypeLayer;
import noobanidus.mods.miniatures.client.renderer.layers.ChargedLayer;
import noobanidus.mods.miniatures.entity.MiniMeEntity;
import noobanidus.mods.miniatures.setup.ClientSetup;
import noobanidus.mods.miniatures.util.NoobUtil;

import java.util.Map;

// TODO:
public class MiniMeRenderer extends HumanoidMobRenderer<MiniMeEntity, MiniMeModel<MiniMeEntity>> {
  private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");

  @SuppressWarnings("unchecked")
  public MiniMeRenderer(EntityRendererProvider.Context context) {
    super(context, new MiniMeModel<>(context.bakeLayer(ClientSetup.MINI_ME), false), 0.5f);
    ModelHolder.init(context);
    this.addLayer(new ItemInHandLayer<>(this));
    this.addLayer(new ArrowRenderTypeLayer<>(context, this));
    this.addLayer(new CustomHeadLayer<>(this, context.getModelSet()));
    this.addLayer(new ElytraLayer<>(this, context.getModelSet()));
    this.addLayer(new BeeStingerRenderTypeLayer<>(this));
    this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ClientSetup.MINI_ME_ARMOR)), new HumanoidModel<>(context.bakeLayer(ClientSetup.MINI_ME_ARMOR))));
    this.addLayer(new ChargedLayer<>(this));
  }

  @Override
  public ResourceLocation getTextureLocation(MiniMeEntity entity) {
    return entity.getGameProfile()
        .map(this::getSkin)
        .orElse(TEXTURE_STEVE);
  }

  private ResourceLocation getSkin(GameProfile gameProfile) {
    if (!gameProfile.isComplete()) {
      return TEXTURE_STEVE;
    } else {
      final Minecraft minecraft = Minecraft.getInstance();
      SkinManager skinManager = minecraft.getSkinManager();
      final Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> loadSkinFromCache = skinManager.getInsecureSkinInformation(gameProfile); // returned map may or may not be typed
      if (loadSkinFromCache.containsKey(MinecraftProfileTexture.Type.SKIN)) {
        return skinManager.registerTexture(loadSkinFromCache.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
      } else {
        return DefaultPlayerSkin.getDefaultSkin(gameProfile.getId());
      }
    }
  }

  @Override
  public void render(MiniMeEntity miniMeEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
    this.model = ModelHolder.miniMe;
    if (miniMeEntity.isSlim() && this.model != ModelHolder.miniMeSlim) {
      this.model = ModelHolder.miniMeSlim;
    }
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
    float scale = miniMeEntity.getMiniScale();
    if (NoobUtil.isNoob(miniMeEntity)) {
      poseStack.scale(1.0975F * scale, 1.0975F * scale, 1.0975F * scale);
    } else {
      poseStack.scale(0.9375F * scale, 0.9375F * scale, 0.9375F * scale);
    }
  }

  @Override
  protected void setupRotations(MiniMeEntity miniMeEntity, PoseStack poseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
    super.setupRotations(miniMeEntity, poseStack, pAgeInTicks, pRotationYaw, pPartialTicks);
    int noob = miniMeEntity.getNoobVariant();
    if (noob == 0) {
      poseStack.translate(0.0D, miniMeEntity.getBbHeight() + 0.3F, 0.0D);
      poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
    } else if (noob == 1) {
      poseStack.translate(0.0D, 0.5F, 0.0D);
    } else if (noob == 6) {
      poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0f));
    } else if (noob == 7) {
      poseStack.mulPose(Vector3f.YP.rotationDegrees(-90.0f));
    } else if (noob == 8) {
      poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0f));
    }
  }
}
