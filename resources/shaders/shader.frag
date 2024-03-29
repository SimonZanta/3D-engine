#version 330

in vec2 outTexCoord;
in vec3 fragPos;
in vec3 fragNormal;
in mat4 fragModelViewMatrix;

out vec4 fragColor;

struct Material
{
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	int hasTexture;
	int hasNormalMap; // implement
	float reflectance;
};

struct PointLight{
	vec3 color;
	vec3 position;
	float intensity;
	float constant;
	float linear;
	float exponent;
};

uniform sampler2D texture_sampler;
uniform sampler2D normalMap; // implement
uniform vec3 ambientLight;
uniform Material material;
uniform float specularPower;
uniform PointLight pointLight;

vec4 ambientC;
vec4 diffuseC;
vec4 speculrC;

void setupColours(Material material, vec2 textCoord)
{
	if (material.hasTexture == 1)
	{
		ambientC = texture(texture_sampler, textCoord);
		diffuseC = ambientC;
		speculrC = ambientC;
	}
	else
	{
		ambientC = material.ambient;
		diffuseC = material.diffuse;
		speculrC = material.specular;
	}
}

vec4 calcLightColor(vec3 lightColor, float intensity, vec3 position, vec3 toLightDir, vec3 normal){
	vec4 diffuseColor = vec4(0,0,0,0);
	vec4 specColor = vec4(0,0,0,0);

	float diffuseFactor = max(dot(normal, toLightDir), 0.0);
	diffuseColor = diffuseC * vec4(lightColor, 1.0) * intensity * diffuseFactor;

	vec3 cameraDir = normalize(-position);
	vec3 fromLightDir = -toLightDir;
	vec3 reflectedLight =  normalize(reflect(fromLightDir, normal));
	float specularFactor = max(dot(cameraDir, reflectedLight), 0.0);
	specularFactor = pow(specularFactor, specularPower);
	specColor = speculrC * intensity * specularFactor * material.reflectance * vec4(lightColor, 1.0);

	return (diffuseColor + specColor);
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal){
	vec3 lightDir = light.position - position;
	vec3 toLightDir = normalize(lightDir);
	vec4 lighColor = calcLightColor(light.color, light.intensity, position, toLightDir, normal);

	float distance = length(lightDir);
	float attenInv = light.constant + light.linear * distance + light.exponent * distance * distance;
	return lighColor / attenInv;
}

vec3 calcNormal(Material material, vec3 normal, vec2 textureCoord, mat4 MVM){
	vec3 newNormal = normal;
	if(material.hasNormalMap == 1){
		newNormal = texture(normalMap, textureCoord).rgb;
		newNormal = normalize((newNormal * 2) - 1);
		newNormal = normalize(MVM * vec4(newNormal, 0.0)).xyz;
	}
	return newNormal;
}

void main()
{
	setupColours(material, outTexCoord);

	vec3 calulatedNormal = calcNormal(material, fragNormal, outTexCoord, fragModelViewMatrix);

	vec4 diffuseSpecComp = calcPointLight(pointLight, fragPos, calulatedNormal);

	fragColor = ambientC * vec4(ambientLight, 1) + diffuseSpecComp;
}