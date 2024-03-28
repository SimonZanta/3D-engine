#version 330

// input from the vertex buffer
layout (location =0) in vec3 inPosition;
//layout (location =1) in vec3 inColor;
layout (location =1) in vec2 inTexCoord;
layout (location =2) in vec3 inVertexNormal;
// out has to have same name as input in fragment shader
//out vec3 exColor;

out vec2 outTexCoord;
out vec3 fragNormal;
out vec3 fragPos;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main() {
    vec4 mvPos = modelViewMatrix * vec4(inPosition, 1.0);
    gl_Position = projectionMatrix * mvPos;
    outTexCoord = inTexCoord;
    fragNormal = normalize(modelViewMatrix * vec4(inVertexNormal, 0.0)).xyz;
    fragPos = mvPos.xyz;
}