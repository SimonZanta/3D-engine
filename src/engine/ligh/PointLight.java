package engine.ligh;

import engine.item.GameItem;
import engine.item.objects.Piramid;
import org.joml.Vector3f;

import java.io.IOException;

public class PointLight {
    private Vector3f color;

    private Vector3f position;

    protected float intensity;

    private Attenuation attenuation;

    private final GameItem gameItem;

    public PointLight(Vector3f color, Vector3f position, float intensity) throws IOException {
        attenuation = new Attenuation(1, 0, 0);
        this.color = color;
        this.position = position;
        this.intensity = intensity;
        this.gameItem = new Piramid().createGameItem();
        this.gameItem.setPosition(position);
        this.gameItem.setRotation(-90,0,45);
        this.gameItem.setScale(0.5f);
    }

    public PointLight(Vector3f color, Vector3f position, float intensity, Attenuation attenuation) throws IOException {
        this(color, position, intensity);
        this.attenuation = attenuation;
    }

    public PointLight(PointLight pointLight) throws IOException {
        this(new Vector3f(pointLight.getColor()), new Vector3f(pointLight.getPosition()),
                pointLight.getIntensity(), pointLight.getAttenuation());
    }

    public GameItem getGameItem() {
        return gameItem;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        this.gameItem.setPosition(position);
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public Attenuation getAttenuation() {
        return attenuation;
    }

    public void setAttenuation(Attenuation attenuation) {
        this.attenuation = attenuation;
    }

    public static class Attenuation {

        private float constant;

        private float linear;

        private float exponent;

        public Attenuation(float constant, float linear, float exponent) {
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }

        public float getConstant() {
            return constant;
        }

        public void setConstant(float constant) {
            this.constant = constant;
        }

        public float getLinear() {
            return linear;
        }

        public void setLinear(float linear) {
            this.linear = linear;
        }

        public float getExponent() {
            return exponent;
        }

        public void setExponent(float exponent) {
            this.exponent = exponent;
        }
    }
}
