#version 330

//in variable has to have same name as out variable from vertex shader
//in  vec3 exColor;

// sequence of defining uniforms is strict
// you cant create sequence in a way it be defined before its struct
// otherwise black box in center of screen will appear
in vec2 outTexCoord;
//light
in vec3 outmvVertexNormal;
in vec3 outmvVertexPos;

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
	if(material.hasTexture == 1){
		ambientC = texture(tex_sampler, outTexCoord);
		diffC = ambientC;
		specC = ambientC;
	}else{
		ambientC = material.ambient;
		diffC = material.diffuse;
		specC = material.specular;
	}
}

vec4 calcPointLight(PointLight pointLight, vec3 normal, vec3 pos){
	vec4 diffColor = vec4(0,0,0,0);
	vec4 specColor = vec4(0,0,0,0);

	vec3 lightDir = pointLight.position - pos;
	vec3 toLightSrc = normalize(lightDir);
	float diffFactor = max(dot(normal, toLightSrc), 0.0);
	diffColor = diffC * vec4(pointLight.color, 1.0) * pointLight.intensity * diffFactor;

	vec3 cameraDir = normalize(-pos);
	vec3 fromLightSrc = -toLightSrc;
	vec3 reflectLight = normalize(reflect(fromLightSrc, normal));
	float specFactor = max(dot(cameraDir, reflectLight), 0.0);
	specFactor = pow(specFactor, specularPower);
	specColor = specC * specFactor * material.reflectance * vec4(pointLight.color, 1.0);

	float dist = length(lightDir);
	float attenInv = pointLight.att.constant + pointLight.att.linear * dist + pointLight.att.exponent * dist * dist;
	return (diffColor + specColor) / attenInv;
}

void main()
{
//	if(useColor == 1){
//		fragColor = vec4(color, 1);
//	}else{
//		fragColor = texture(tex_sampler, outTexCoord);
//	}

	setupCol(material, outTexCoord);
	vec4 diffSpecComp = calcPointLight(pointLight, outmvVertexPos, outmvVertexNormal);
	fragColor = ambientC * vec4(ambientLight, 1) + diffSpecComp;
}