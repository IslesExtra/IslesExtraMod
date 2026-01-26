{
  description = "Nix Tooling for IslesExtra-Fabric";
  inputs.nixpkgs.url = "github:nixos/nixpkgs?ref=nixos-unstable";

  outputs =
    { self, nixpkgs }:
    let
      system = "x86_64-linux";
      pkgs = import nixpkgs { inherit system; };
    in
    {
      apps.${system} = {
        default = self.apps.${system}.runClient;
        runClient = {
          type = "app";
          program = "${pkgs.writeShellScriptBin "run-client" ''
            export LD_LIBRARY_PATH=${pkgs.libGL}/lib:$LD_LIBRARY_PATH
            ${pkgs.gradle_9}/bin/gradle runClient -Ddevauth.enabled=true -Ddevauth.configDir="$HOME/.config/devauth"
          ''}/bin/run-client";
        };
        updateJavaHome =
          let
            current = builtins.fromJSON (builtins.readFile ./.vscode/settings.json);
            updated = current // {
              "java.jdt.ls.java.home" = "${pkgs.openjdk21}/lib/openjdk/";
              "jdk.jdkhome" = "${pkgs.openjdk21}/lib/openjdk/";
            };
            new = builtins.toJSON updated;
          in
          {
            type = "app";
            program = "${pkgs.writeShellScriptBin "update-java-home" ''
              echo '${new}' | ${pkgs.jq}/bin/jq . > ./.vscode/settings.json
            ''}/bin/update-java-home";
          };

        updateLaunchConfigs =
          let
            current = builtins.fromJSON (builtins.readFile ./.vscode/launch.json);
            updatedConfigurations = map (
              attrs:
              attrs
              // {
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
          {
            type = "app";
            program = "${pkgs.writeShellScriptBin "update-launch-configs" ''
              echo '${new}' | ${pkgs.jq}/bin/jq . > ./.vscode/launch.json
            ''}/bin/update-launch-configs";
          };
      };
    };
}
