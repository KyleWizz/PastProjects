#project to predict certain variations that have an effect. Very early stages. Much research must be done
import torch
import torch.nn as nn
import torch.nn.functional as f
import torch.optim as optim
import torchvision
from torchvision import datasets, transforms
import numpy as np

class Net(nn.Module):
    def __init__(self):
        super().__init__()

