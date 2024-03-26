#version 330

// input from the vertex buffer
layout (location =0) in vec3 inPosition;
//layout (location =1) in vec3 inColor;
layout (location =1) in vec2 inTexCoord;
// out has to have same name as input in fragment shader
//out vec3 exColor;

out vec2 outTexCoord;

uniform mat4 projectionMatrix;
uniform mat4 worldMatrix;

void main() {
    gl_Position = projectionMatrix * worldMatrix * vec4(inPosition, 1.0);
    outTexCoord = inTexCoord;
}