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

        updateLaunchConfigsIdea = {
          type = "app";
          program = let
            libPath = pkgs.lib.makeLibraryPath [
              pkgs.libGL
              pkgs.xorg.libX11
              pkgs.xorg.libXcursor
              pkgs.xorg.libXrandr
              pkgs.xorg.libXi
              pkgs.libxkbcommon
            ];
            jqFilter = ".component.configuration.envs.env |= (if type == \"array\" then map(select(.[\"+name\"] == \"LD_LIBRARY_PATH\") .[\"+value\"] = \"${libPath}\") else .[\"+value\"] = \"${libPath}\" end)";
          in
          "${pkgs.writeShellScriptBin "update-idea-config" ''
            XML_FILE="./.idea/runConfigurations/Minecraft_Client.xml"
            if [ ! -f "$XML_FILE" ]; then
              echo "Fehler: $XML_FILE nicht gefunden!"
              exit 1
            fi

            ${pkgs.yq-go}/bin/yq -p=xml -o=json . "$XML_FILE" | \
            ${pkgs.jq}/bin/jq '${jqFilter}' | \
            ${pkgs.yq-go}/bin/yq -p=json -o=xml > "$XML_FILE"
          ''}/bin/update-idea-config";
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
