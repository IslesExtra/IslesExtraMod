package com.kyllian.skyblockisles.islesextra.entity.custom.queen_bee;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class QueenBeeModel extends EntityModel<QueenBeeEntity> {
	private final ModelPart body;
	private final ModelPart hip;
	public QueenBeeModel(ModelPart root) {
		this.body = root.getChild("body");
		this.hip = root.getChild("hip");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-9.5F, -9.4167F, -12.5F, 19.0F, 17.0F, 25.0F, new Dilation(0.0F))
		.uv(170, 64).cuboid(6.5F, 1.5833F, -16.5F, 4.0F, 7.0F, 4.0F, new Dilation(0.0F))
		.uv(34, 49).cuboid(6.5F, 0.0833F, -14.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(17, 14).cuboid(-8.5F, 0.0833F, -14.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(166, 169).cuboid(-10.5F, 1.5833F, -16.5F, 4.0F, 7.0F, 4.0F, new Dilation(0.0F))
		.uv(116, 41).cuboid(-5.5F, -12.4167F, -10.5F, 11.0F, 3.0F, 20.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -16.5833F, -0.5F));

		ModelPartData left_leg_front = body.addChild("left_leg_front", ModelPartBuilder.create().uv(149, 152).cuboid(-3.629F, -0.1612F, -3.6415F, 7.0F, 10.0F, 7.0F, new Dilation(0.0F))
		.uv(128, 125).cuboid(3.371F, 4.8388F, -3.1415F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(90, 61).cuboid(0.871F, 4.8388F, -4.6415F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(90, 19).cuboid(-2.629F, 4.8388F, -4.6415F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(84, 61).cuboid(-2.629F, 4.8388F, 3.3585F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(88, 23).cuboid(0.871F, 4.8388F, 3.3585F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(128, 108).cuboid(3.371F, 4.8388F, 0.8585F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(95, 0).cuboid(-4.629F, 4.8388F, 0.8585F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(128, 91).cuboid(-4.629F, 4.8388F, -3.1415F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(8.629F, 6.9946F, -13.6085F));

		left_leg_front.addChild("cube_r1", ModelPartBuilder.create().uv(171, 122).cuboid(-0.75F, 2.75F, -3.25F, 4.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-1.129F, 8.8388F, -0.1415F, 0.3927F, 0.0F, 0.0F));

		left_leg_front.addChild("cube_r2", ModelPartBuilder.create().uv(165, 0).cuboid(-0.75F, 0.75F, -3.25F, 5.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-1.7098F, 6.9746F, -0.1415F, 0.3927F, 0.0F, 0.0F));

		ModelPartData right_leg_front = body.addChild("right_leg_front", ModelPartBuilder.create().uv(128, 125).cuboid(-3.371F, -0.1612F, -3.6415F, 7.0F, 10.0F, 7.0F, new Dilation(0.0F))
		.uv(66, 59).cuboid(-4.371F, 4.8388F, -3.1415F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(78, 61).cuboid(-2.871F, 4.8388F, -4.6415F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(72, 61).cuboid(0.629F, 4.8388F, -4.6415F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(65, 46).cuboid(0.629F, 4.8388F, 3.3585F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(45, 42).cuboid(-2.871F, 4.8388F, 3.3585F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(65, 42).cuboid(-4.371F, 4.8388F, 0.8585F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(63, 0).cuboid(3.629F, 4.8388F, 0.8585F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(3.629F, 4.8388F, -3.1415F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-8.629F, 6.9946F, -13.6085F));

		right_leg_front.addChild("cube_r3", ModelPartBuilder.create().uv(128, 71).cuboid(-3.25F, 2.75F, -3.25F, 4.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(1.129F, 8.8388F, -0.1415F, 0.3927F, 0.0F, 0.0F));

		right_leg_front.addChild("cube_r4", ModelPartBuilder.create().uv(96, 15).cuboid(-4.25F, 0.75F, -3.25F, 5.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(1.7098F, 6.9746F, -0.1415F, 0.3927F, 0.0F, 0.0F));

		ModelPartData left_leg_mid = body.addChild("left_leg_mid", ModelPartBuilder.create().uv(128, 142).cuboid(-3.629F, -0.1612F, -3.6415F, 7.0F, 10.0F, 7.0F, new Dilation(0.0F))
		.uv(179, 101).cuboid(2.621F, 3.5888F, -1.6415F, 2.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(8.629F, 5.9946F, -4.6085F));

		left_leg_mid.addChild("cube_r5", ModelPartBuilder.create().uv(170, 152).cuboid(-1.5F, 3.25F, -4.0F, 4.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-1.129F, 8.8388F, -0.1415F, 0.0F, 0.0F, 0.3927F));

		left_leg_mid.addChild("cube_r6", ModelPartBuilder.create().uv(164, 130).cuboid(-0.5F, 0.75F, -3.25F, 5.0F, 7.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-1.7098F, 6.9746F, -0.1415F, 0.0F, 0.0F, 0.3927F));

		ModelPartData right_leg_mid = body.addChild("right_leg_mid", ModelPartBuilder.create().uv(128, 108).cuboid(-3.371F, -0.1612F, -3.6415F, 7.0F, 10.0F, 7.0F, new Dilation(0.0F))
		.uv(179, 32).cuboid(-4.621F, 3.5888F, -1.6415F, 2.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-8.629F, 5.9946F, -4.6085F));

		right_leg_mid.addChild("cube_r7", ModelPartBuilder.create().uv(128, 64).cuboid(-2.5F, 3.25F, -4.0F, 4.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(1.129F, 8.8388F, -0.1415F, 0.0F, 0.0F, -0.3927F));

		right_leg_mid.addChild("cube_r8", ModelPartBuilder.create().uv(128, 159).cuboid(-4.5F, 0.75F, -3.25F, 5.0F, 7.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(1.7098F, 6.9746F, -0.1415F, 0.0F, 0.0F, -0.3927F));

		ModelPartData left_leg_back = body.addChild("left_leg_back", ModelPartBuilder.create().uv(177, 159).cuboid(2.621F, 3.5888F, -1.8915F, 2.0F, 7.0F, 3.0F, new Dilation(0.0F))
		.uv(128, 91).cuboid(-3.629F, -0.1612F, -3.8915F, 7.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(8.629F, 5.9946F, 6.6415F));

		left_leg_back.addChild("cube_r9", ModelPartBuilder.create().uv(120, 35).cuboid(-1.5F, 3.25F, 7.25F, 4.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-1.129F, 8.8388F, -11.3915F, 0.0F, 0.0F, 0.3927F));

		left_leg_back.addChild("cube_r10", ModelPartBuilder.create().uv(158, 47).cuboid(-0.5F, 0.75F, 8.0F, 5.0F, 7.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-1.7098F, 6.9746F, -11.3915F, 0.0F, 0.0F, 0.3927F));

		ModelPartData right_leg_back = body.addChild("right_leg_back", ModelPartBuilder.create().uv(0, 42).cuboid(-4.621F, 3.5888F, -1.8915F, 2.0F, 7.0F, 3.0F, new Dilation(0.0F))
		.uv(44, 42).cuboid(-3.371F, -0.1612F, -3.8915F, 7.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(-8.629F, 5.9946F, 6.6415F));

		right_leg_back.addChild("cube_r11", ModelPartBuilder.create().uv(34, 42).cuboid(-2.5F, 3.25F, 7.25F, 4.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(1.129F, 8.8388F, -11.3915F, 0.0F, 0.0F, -0.3927F));

		right_leg_back.addChild("cube_r12", ModelPartBuilder.create().uv(158, 35).cuboid(-4.5F, 0.75F, 8.0F, 5.0F, 7.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(1.7098F, 6.9746F, -11.3915F, 0.0F, 0.0F, -0.3927F));

		ModelPartData h_head = body.addChild("h_head", ModelPartBuilder.create().uv(128, 64).cuboid(-7.0F, -5.0F, -15.0F, 14.0F, 13.0F, 14.0F, new Dilation(0.0F))
		.uv(156, 104).cuboid(-8.0F, -3.0F, -16.0F, 5.0F, 8.0F, 6.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(3.0F, -3.0F, -16.0F, 5.0F, 8.0F, 6.0F, new Dilation(0.0F))
		.uv(0, 1).cuboid(5.25F, -2.0F, -16.25F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-5.5F, -2.0F, -16.25F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.4167F, -11.5F));

		h_head.addChild("cube_r13", ModelPartBuilder.create().uv(173, 42).cuboid(-4.25F, -0.25F, -2.5F, 2.0F, 2.0F, 5.0F, new Dilation(0.0F))
		.uv(173, 54).cuboid(8.0F, -0.25F, -2.5F, 2.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, 6.5F, -16.5F, 0.3927F, 0.0F, 0.0F));

		h_head.addChild("cube_r14", ModelPartBuilder.create().uv(143, 36).cuboid(-4.0F, 0.5F, -1.5F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, 6.5F, -16.5F, 0.0F, -0.7854F, 0.0F));

		h_head.addChild("cube_r15", ModelPartBuilder.create().uv(153, 35).cuboid(1.75F, 0.5F, -1.5F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, 6.5F, -16.5F, 0.0F, 0.7854F, 0.0F));

		ModelPartData h_left_ant = h_head.addChild("h_left_ant", ModelPartBuilder.create(), ModelTransform.pivot(3.5F, -3.5F, -15.0F));

		h_left_ant.addChild("cube_r16", ModelPartBuilder.create().uv(152, 179).cuboid(0.5F, -10.75F, -0.25F, 2.0F, 8.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.5F, -3.5F, -0.75F, 0.7854F, 0.0F, 0.0F));

		h_left_ant.addChild("cube_r17", ModelPartBuilder.create().uv(131, 0).cuboid(1.0F, -4.0F, -1.0F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.5F, -3.5F, -0.75F, 0.3927F, 0.0F, 0.0F));

		ModelPartData h_right_ant = h_head.addChild("h_right_ant", ModelPartBuilder.create(), ModelTransform.pivot(-3.5F, -3.5F, -15.0F));

		h_right_ant.addChild("cube_r18", ModelPartBuilder.create().uv(144, 179).cuboid(-2.5F, -10.75F, -0.25F, 2.0F, 8.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(1.5F, -3.5F, -0.75F, 0.7854F, 0.0F, 0.0F));

		h_right_ant.addChild("cube_r19", ModelPartBuilder.create().uv(116, 15).cuboid(-2.0F, -4.0F, -1.0F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.5F, -3.5F, -0.75F, 0.3927F, 0.0F, 0.0F));

		ModelPartData h_crown = h_head.addChild("h_crown", ModelPartBuilder.create().uv(128, 171).cuboid(4.0F, -0.5F, -0.8333F, 1.0F, 4.0F, 7.0F, new Dilation(0.0F))
		.uv(171, 111).cuboid(-5.0F, -0.5F, -0.8333F, 1.0F, 4.0F, 7.0F, new Dilation(0.0F))
		.uv(44, 59).cuboid(-5.0F, -0.5F, -1.8333F, 10.0F, 4.0F, 1.0F, new Dilation(0.0F))
		.uv(108, 61).cuboid(-1.0F, -2.5F, -1.8333F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(96, 61).cuboid(-5.0F, -2.5F, -1.8333F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(102, 61).cuboid(3.0F, -2.5F, -1.8333F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -9.5F, -11.1667F));

		ModelPartData left_wing = body.addChild("left_wing", ModelPartBuilder.create(), ModelTransform.pivot(6.0F, -10.0F, 0.0F));

		left_wing.addChild("cube_r20", ModelPartBuilder.create().uv(0, 64).cuboid(0.0F, -61.0F, -34.5F, 0.0F, 64.0F, 64.0F, new Dilation(0.0F)), ModelTransform.of(13.4454F, 12.0F, 9.5459F, 0.0F, 0.7854F, 0.0F));

		ModelPartData right_wing = body.addChild("right_wing", ModelPartBuilder.create(), ModelTransform.pivot(-6.0F, -10.0F, 0.0F));

		right_wing.addChild("cube_r21", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, -61.0F, -34.5F, 0.0F, 64.0F, 64.0F, new Dilation(0.0F)), ModelTransform.of(-13.4454F, 12.0F, 9.5459F, 0.0F, -0.7854F, 0.0F));

		ModelPartData hip = modelPartData.addChild("hip", ModelPartBuilder.create().uv(72, 26).cuboid(-8.0F, -7.0F, -5.0F, 16.0F, 19.0F, 16.0F, new Dilation(0.0F))
		.uv(149, 135).cuboid(8.0F, 0.0F, 2.0F, 4.0F, 5.0F, 7.0F, new Dilation(0.0F))
		.uv(149, 118).cuboid(-12.0F, 0.0F, 2.0F, 4.0F, 5.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -18.5F, 12.0F, -0.3927F, 0.0F, 0.0F));

		hip.addChild("cube_r22", ModelPartBuilder.create().uv(72, 20).cuboid(2.25F, 12.0F, 6.0F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(63, 19).cuboid(-5.25F, 12.0F, 6.0F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(81, 19).cuboid(2.25F, -7.0F, 6.0F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(134, 35).cuboid(-5.25F, -7.0F, 6.0F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(165, 11).cuboid(-8.0F, 7.75F, 6.0F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F))
		.uv(164, 119).cuboid(-8.0F, 2.0F, 6.0F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F))
		.uv(16, 0).cuboid(-8.0F, -3.75F, 6.0F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F))
		.uv(137, 171).cuboid(7.0F, 7.75F, 6.0F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F))
		.uv(173, 11).cuboid(7.0F, 2.0F, 6.0F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F))
		.uv(179, 129).cuboid(7.0F, -3.75F, 6.0F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F))
		.uv(120, 0).cuboid(-7.0F, -6.0F, -2.0F, 14.0F, 18.0F, 17.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.5F, 9.0F, -0.3927F, 0.0F, 0.0F));

		ModelPartData tail = hip.addChild("tail", ModelPartBuilder.create().uv(0, 42).cuboid(-5.5F, -6.6287F, -1.4645F, 12.0F, 12.0F, 10.0F, new Dilation(0.0F))
		.uv(146, 169).cuboid(-3.5F, -4.6287F, 8.5355F, 8.0F, 8.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 14).cuboid(-2.5F, -3.6287F, 10.5355F, 6.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, 9.1F, 20.55F, -0.7854F, 0.0F, 0.0F));

		tail.addChild("sting", ModelPartBuilder.create().uv(63, 0).cuboid(-6.0F, -7.25F, 1.5F, 13.0F, 13.0F, 6.0F, new Dilation(0.0F))
		.uv(101, 0).cuboid(-5.0F, -6.25F, 7.5F, 11.0F, 11.0F, 4.0F, new Dilation(0.0F))
		.uv(167, 143).cuboid(-2.0F, -3.25F, 15.5F, 5.0F, 5.0F, 4.0F, new Dilation(0.0F))
		.uv(156, 91).cuboid(-4.0F, -5.25F, 11.5F, 9.0F, 9.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.1213F, 11.7855F));
		return TexturedModelData.of(modelData, 256, 256);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		hip.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void setAngles(QueenBeeEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}
}