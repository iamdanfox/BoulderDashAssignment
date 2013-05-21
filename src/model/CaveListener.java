package model;

public interface CaveListener {
	public void wonStateChanged();
	public void lostStateChanged();
	public void diamondTargetChanged();
	public void gridChanged();
	public void dimensionsChanged();
	public void frozenStateChanged();
}
