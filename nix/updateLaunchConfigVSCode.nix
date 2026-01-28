{
  pkgs ? import <nixpkgs> { }
}:
let
  path = ../.vscode/launch.json;
  current = builtins.fromJSON (builtins.readFile path);
  updatedConfigurations = map (attrs:
    pkgs.lib.recursiveUpdate attrs {
      env.LD_LIBRARY_PATH = "${pkgs.lib.makeLibraryPath ([
        pkgs.libGL
        pkgs.xorg.libX11
        pkgs.xorg.libXcursor
        pkgs.xorg.libXrandr
        pkgs.xorg.libXi
        pkgs.libxkbcommon
      ])}";
    }
  ) current.configurations;

  updated = current // {
    configurations = updatedConfigurations;
  };

  new = builtins.toJSON updated;
in
pkgs.writeShellScriptBin "update-launch-configs-vscode" ''
  echo '${new}' | ${pkgs.lib.getExe pkgs.jq} . > "${toString path}"
''