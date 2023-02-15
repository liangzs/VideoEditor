uniform mat4 u_Matrix;
attribute vec3 a_Position;
attribute vec3 a_Color;
attribute float a_PointSize;
attribute float a_Alpha;
varying float v_Alpha;
varying vec3 v_Color;


void main()                    
{                                	  	  
    v_Color = a_Color;
    v_Alpha = a_Alpha;
    gl_PointSize = a_PointSize;
    gl_Position = u_Matrix * vec4(a_Position, 1.0);
}
