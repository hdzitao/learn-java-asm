package hdzitao;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class HelloWorldDump {

    public static byte[] dump() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, "sample/HelloWorld", null, "java/lang/Object", null);

        {
            MethodVisitor init = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            init.visitCode();
            init.visitVarInsn(ALOAD, 0);
            init.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            init.visitInsn(RETURN);
            init.visitMaxs(1, 1);
            init.visitEnd();
        }
        {
            MethodVisitor toString = cw.visitMethod(ACC_PUBLIC, "toString", "()Ljava/lang/String;", null, null);
            toString.visitCode();
            toString.visitLdcInsn("This is a HelloWorld object.");
            toString.visitInsn(ARETURN);
            toString.visitMaxs(1, 1);
            toString.visitEnd();
        }

        cw.visitEnd();

        return cw.toByteArray();
    }

    public static class InnerClassLoader extends ClassLoader {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            if ("sample.HelloWorld".equals(name)) {
                byte[] bytes = dump();
                return defineClass(name, bytes, 0, bytes.length);
            }

            throw new ClassNotFoundException("Class Not Found: " + name);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Object helloWord = new InnerClassLoader().findClass("sample.HelloWorld").newInstance();
        System.out.println(helloWord);
    }
}
