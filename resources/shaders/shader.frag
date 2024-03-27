#version 330

//in variable has to have same name as out variable from vertex shader
//in  vec3 exColor;
in vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D tex_sampler;
uniform vec3 color;
uniform int useColor;

void main()
{
	if(useColor == 1){
		fragColor = vec4(color, 1);
	}else{
		fragColor = texture(tex_sampler, outTexCoord);
	}
}