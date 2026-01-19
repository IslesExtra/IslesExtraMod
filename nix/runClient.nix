{
  writeShellScriptBin,
  gradle_9,
  libGL,
  ...
}:
{
  type = "app";
  program = "${writeShellScriptBin "run-client" ''
    export LD_LIBRARY_PATH=${libGL}/lib:$LD_LIBRARY_PATH
    ${gradle_9}/bin/gradle runClient
  ''}/bin/run-client";
}