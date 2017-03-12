package com.dx168.fastdex.build.util

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 source class:
 ''''''
 public class MainActivity {

 }
 '''''

 dest class:
 ''''''
 import com.dx168.fastdex.antilazyload.AntilazyLoad;

 public class MainActivity {
     public MainActivity() {
        System.out.println(Antilazyload.class);
     }
 }
 ''''''
 * Created by tong on 17/10/3.
 */
public class ClassInject implements Opcodes {
    public static final void inject(File source, File dest) {
        byte[] classBytes = FileUtils.readContents(source)
        ClassReader classReader = new ClassReader(classBytes);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new MyClassVisitor(classWriter);
        classReader.accept(classVisitor, Opcodes.ASM5);


        File parent = dest.getParentFile();
        if (parent != null && (!parent.exists())) {
            parent.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(dest);
        fos.write(classWriter.toByteArray());
        fos.close();
    }

    private static class MyClassVisitor extends ClassVisitor {
        public MyClassVisitor(ClassVisitor classVisitor) {
            super(Opcodes.ASM5, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int access,
                                         String name,
                                         String desc,
                                         String signature,
                                         String[] exceptions) {
            if ("<init>".equals(name) && "()V".equals(desc)) {
                //get origin method
                MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
                //System.out.println(name + " | " + desc + " | " + signature);
                MethodVisitor newMethod = new AsmMethodVisit(mv);
                return newMethod;
            } else {
                return super.visitMethod(access, name, desc, signature, exceptions);
            }
        }
    }

    static class AsmMethodVisit extends MethodVisitor {
        public AsmMethodVisit(MethodVisitor mv) {
            super(Opcodes.ASM5, mv);
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == Opcodes.RETURN) {
                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mv.visitFieldInsn(GETSTATIC, "com/dx168/fastdex/antilazyload/AntilazyLoad", "str", "Ljava/lang/String;");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            }
            super.visitInsn(opcode);
        }
    }
}
