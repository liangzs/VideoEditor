uniform mat4 u_Matrix;
uniform float u_Time;

attribute vec3 a_Position;  
attribute vec3 a_DirectionVector;
attribute float a_ParticleStartTime;
attribute float a_PointSize;
attribute float a_Alpha;
varying float v_Alpha;
void main()
{                                	  	  
    float v_ElapsedTime = u_Time - a_ParticleStartTime;
    float gravityFactor = v_ElapsedTime * v_ElapsedTime / 10.0;
    vec3 currentPosition = a_Position + (a_DirectionVector * gravityFactor);
    gl_Position = u_Matrix * vec4(currentPosition, 1.0);
    gl_PointSize =a_PointSize;
    v_Alpha=a_Alpha;
}   
