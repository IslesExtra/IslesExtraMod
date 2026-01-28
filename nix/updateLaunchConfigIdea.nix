{
  pkgs ? import <nixpkgs> { }
}:
let
  path = ../.idea/runConfigurations/Minecraft_Client.xml;

  jsonCurrent = pkgs.runCommand "xml-to-json" { 
    nativeBuildInputs = [ pkgs.yq-go ]; 
  } ''
    yq -p=xml -o=json . ${path} > $out
  '';

  current = builtins.fromJSON (builtins.readFile jsonCurrent);

  updated = pkgs.lib.recursiveUpdate current {
    component.configuration.envs.env = [
      {
        "+@name" = "LD_LIBRARY_PATH";
        "+@value" = "${pkgs.lib.makeLibraryPath ([
          pkgs.libGL
          pkgs.xorg.libX11
          pkgs.xorg.libXcursor
          pkgs.xorg.libXrandr
          pkgs.xorg.libXi
          pkgs.libxkbcommon
        ])}";
      }
    ];
  };

  newJson = builtins.toJSON updated;
in
pkgs.writeShellScriptBin "update-launch-configs-idea" ''
  cat << 'EOF' | ${pkgs.yq-go}/bin/yq -p=json -o=xml > "${toString path}"
  ${newJson}
EOF
''