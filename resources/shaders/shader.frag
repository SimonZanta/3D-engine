#version 330

//in variable has to have same name as out variable from vertex shader
in  vec3 exColor;
out vec4 fragColor;

void main()
{
	fragColor = vec4(exColor, 1.0);
}