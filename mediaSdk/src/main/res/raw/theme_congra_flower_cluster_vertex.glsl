uniform mat4 u_Matrix;
uniform float u_Time;

attribute vec3 a_Position;  
attribute float a_Paticle;
attribute float a_ParticleStartTime;
attribute float a_PointSize;
varying float v_ElapsedTime;
varying float v_Paticle;
void main()
{                                	  	  
    v_Paticle=a_Paticle;
    v_ElapsedTime = u_Time - a_ParticleStartTime;
    gl_Position = u_Matrix * vec4(a_Position, 1.0);
    gl_PointSize = a_PointSize;
}
