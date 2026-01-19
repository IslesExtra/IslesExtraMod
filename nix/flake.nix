{
  description = "Nix Tooling for IslesExtra-Fabric";
  inputs.nixpkgs.url = "github:nixos/nixpkgs?ref=nixos-unstable";

  outputs = { self, nixpkgs }:
  let 
    system = "x86_64-linux";
    pkgs = import nixpkgs { inherit system; };
  in
  {
    apps.${system} = {
      runClient = pkgs.callPackage ./runClient.nix { };
    };
  };
}
