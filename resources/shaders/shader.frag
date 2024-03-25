#version 330

//in variable has to have same name as out variable from vertex shader
//in  vec3 exColor;
in vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D tex_sampler;

void main()
{
//	fragColor = vec4(exColor, 1.0);
	fragColor = texture(tex_sampler, outTexCoord);
}