#version 330

//in variable has to have same name as out variable from vertex shader
//in  vec3 exColor;

// sequence of defining uniforms is strict
// you cant create sequence in a way it be defined before its struct
// otherwise black box in center of screen will appear
in vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D tex_sampler;
uniform vec3 color;
uniform int useColor;

vec4 ambientC;
vec4 diffC;
vec4 specC;

struct Attenuation{
	float constant;
	float linear;
	float exponent;
};

struct PointLight{
	vec3 color;
	vec3 position;
	float intensity;
	Attenuation att;
};

struct Material{
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	int hasTexture;
	float reflectance;
};

uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform PointLight pointLight;
uniform vec3 camera_pos;

void setupCol(Material material, vec2 outTexCoord){

}

void main()
{
	if(useColor == 1){
		fragColor = vec4(color, 1);
	}else{
		fragColor = texture(tex_sampler, outTexCoord);
	}
}