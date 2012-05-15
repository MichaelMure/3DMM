#import "Common/ShaderLib/MultiSample.glsllib"
 
uniform COLORTEXTURE m_Texture;
in vec2 texCoord;
 
uniform vec4 m_offsets;
uniform vec4 m_gains;
 
void main() {
    vec4 texVal = texture2D(m_Texture, texCoord);
    gl_FragColor = texVal.a * m_offsets + m_gains * texVal;
}
