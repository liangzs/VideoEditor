uniform mat4 u_Matrix;
uniform float u_Time;

attribute vec3 a_Position;  
attribute float a_Paticle;
attribute float a_PointSize;
attribute float a_Alpha;
varying float v_Paticle;
varying float v_Alpha;

void main()                    
{                                	  	  
    v_Paticle=a_Paticle;
    v_Alpha=a_Alpha;
    gl_Position = u_Matrix * vec4(a_Position, 1.0);
    gl_PointSize = a_PointSize;
}
